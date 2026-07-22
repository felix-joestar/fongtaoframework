package com.fongtaoframework.starter.core.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class TreeUtil {

    private TreeUtil() {
    }

    public static <T, I> List<T> build(
            Collection<T> nodes,
            Function<? super T, ? extends I> idGetter,
            Function<? super T, ? extends I> parentIdGetter,
            BiFunction<? super T, ? super List<T>, ? extends T> childrenMapper
    ) {
        if (nodes == null || nodes.isEmpty()) {
            return List.of();
        }
        Objects.requireNonNull(idGetter, "idGetter must not be null");
        Objects.requireNonNull(parentIdGetter, "parentIdGetter must not be null");
        Objects.requireNonNull(childrenMapper, "childrenMapper must not be null");

        List<T> source = new ArrayList<>(nodes);
        Map<I, T> nodesById = new LinkedHashMap<>();
        for (T node : source) {
            Objects.requireNonNull(node, "tree node must not be null");
            I id = requireId(idGetter.apply(node));
            if (nodesById.putIfAbsent(id, node) != null) {
                throw new IllegalArgumentException("树节点 ID 重复：" + id);
            }
        }

        Map<I, List<T>> childrenByParentId = new LinkedHashMap<>();
        List<T> roots = new ArrayList<>();
        for (T node : source) {
            I parentId = parentIdGetter.apply(node);
            if (parentId == null || !nodesById.containsKey(parentId)) {
                roots.add(node);
            } else {
                childrenByParentId.computeIfAbsent(parentId, key -> new ArrayList<>()).add(node);
            }
        }

        Map<I, VisitState> states = new LinkedHashMap<>();
        List<T> result = new ArrayList<>(roots.size());
        for (T root : roots) {
            result.add(buildNode(root, idGetter, childrenByParentId, childrenMapper, states));
        }
        for (T node : source) {
            I id = idGetter.apply(node);
            if (states.get(id) != VisitState.DONE) {
                buildNode(node, idGetter, childrenByParentId, childrenMapper, states);
            }
        }
        return List.copyOf(result);
    }

    public static <T> List<T> flatten(Collection<T> roots, Function<? super T, ? extends Collection<T>> childrenGetter) {
        return preOrder(roots, childrenGetter);
    }

    public static <T> List<T> preOrder(Collection<T> roots, Function<? super T, ? extends Collection<T>> childrenGetter) {
        Objects.requireNonNull(childrenGetter, "childrenGetter must not be null");
        List<T> result = new ArrayList<>();
        Set<T> path = identitySet();
        for (T root : safeNodes(roots)) {
            preOrder(root, childrenGetter, path, result);
        }
        return List.copyOf(result);
    }

    public static <T> List<T> postOrder(Collection<T> roots, Function<? super T, ? extends Collection<T>> childrenGetter) {
        Objects.requireNonNull(childrenGetter, "childrenGetter must not be null");
        List<T> result = new ArrayList<>();
        Set<T> path = identitySet();
        for (T root : safeNodes(roots)) {
            postOrder(root, childrenGetter, path, result);
        }
        return List.copyOf(result);
    }

    public static <T> List<T> levelOrder(Collection<T> roots, Function<? super T, ? extends Collection<T>> childrenGetter) {
        Objects.requireNonNull(childrenGetter, "childrenGetter must not be null");
        ArrayDeque<T> queue = new ArrayDeque<>(safeNodes(roots));
        Set<T> visited = identitySet();
        List<T> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            T node = Objects.requireNonNull(queue.removeFirst(), "tree node must not be null");
            if (!visited.add(node)) {
                throw new IllegalArgumentException("树结构存在循环");
            }
            result.add(node);
            queue.addAll(safeNodes(childrenGetter.apply(node)));
        }
        return List.copyOf(result);
    }

    public static <T> List<T> sort(
            Collection<T> roots,
            Function<? super T, ? extends Collection<T>> childrenGetter,
            Comparator<? super T> comparator,
            BiFunction<? super T, ? super List<T>, ? extends T> childrenMapper
    ) {
        Objects.requireNonNull(childrenGetter, "childrenGetter must not be null");
        Objects.requireNonNull(comparator, "comparator must not be null");
        Objects.requireNonNull(childrenMapper, "childrenMapper must not be null");
        Set<T> path = identitySet();
        List<T> result = new ArrayList<>();
        for (T root : safeNodes(roots)) {
            result.add(sortNode(root, childrenGetter, comparator, childrenMapper, path));
        }
        result.sort(comparator);
        return List.copyOf(result);
    }

    private static <T, I> T buildNode(
            T node,
            Function<? super T, ? extends I> idGetter,
            Map<I, List<T>> childrenByParentId,
            BiFunction<? super T, ? super List<T>, ? extends T> childrenMapper,
            Map<I, VisitState> states
    ) {
        I id = requireId(idGetter.apply(node));
        VisitState state = states.get(id);
        if (state == VisitState.VISITING) {
            throw new IllegalArgumentException("树结构存在循环，节点 ID：" + id);
        }
        if (state == VisitState.DONE) {
            throw new IllegalArgumentException("树节点存在多个父级，节点 ID：" + id);
        }
        states.put(id, VisitState.VISITING);
        List<T> children = new ArrayList<>();
        for (T child : childrenByParentId.getOrDefault(id, List.of())) {
            children.add(buildNode(child, idGetter, childrenByParentId, childrenMapper, states));
        }
        states.put(id, VisitState.DONE);
        return Objects.requireNonNull(childrenMapper.apply(node, List.copyOf(children)), "childrenMapper must not return null");
    }

    private static <T> void preOrder(
            T node,
            Function<? super T, ? extends Collection<T>> childrenGetter,
            Set<T> path,
            List<T> result
    ) {
        enter(node, path);
        result.add(node);
        for (T child : safeNodes(childrenGetter.apply(node))) {
            preOrder(child, childrenGetter, path, result);
        }
        path.remove(node);
    }

    private static <T> void postOrder(
            T node,
            Function<? super T, ? extends Collection<T>> childrenGetter,
            Set<T> path,
            List<T> result
    ) {
        enter(node, path);
        for (T child : safeNodes(childrenGetter.apply(node))) {
            postOrder(child, childrenGetter, path, result);
        }
        result.add(node);
        path.remove(node);
    }

    private static <T> T sortNode(
            T node,
            Function<? super T, ? extends Collection<T>> childrenGetter,
            Comparator<? super T> comparator,
            BiFunction<? super T, ? super List<T>, ? extends T> childrenMapper,
            Set<T> path
    ) {
        enter(node, path);
        List<T> children = new ArrayList<>();
        for (T child : safeNodes(childrenGetter.apply(node))) {
            children.add(sortNode(child, childrenGetter, comparator, childrenMapper, path));
        }
        children.sort(comparator);
        path.remove(node);
        return Objects.requireNonNull(childrenMapper.apply(node, List.copyOf(children)), "childrenMapper must not return null");
    }

    private static <I> I requireId(I id) {
        if (id == null) {
            throw new IllegalArgumentException("树节点 ID 不能为空");
        }
        return id;
    }

    private static <T> void enter(T node, Set<T> path) {
        Objects.requireNonNull(node, "tree node must not be null");
        if (!path.add(node)) {
            throw new IllegalArgumentException("树结构存在循环");
        }
    }

    private static <T> Collection<T> safeNodes(Collection<T> nodes) {
        return nodes == null ? List.of() : nodes;
    }

    private static <T> Set<T> identitySet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }

    private enum VisitState {
        VISITING,
        DONE
    }
}
