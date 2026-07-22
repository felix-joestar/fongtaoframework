package com.fongtaoframework.starter.core.util;

import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TreeUtilTest {

    @Test
    void buildShouldPreserveOrderAttachImmutableChildrenAndTreatOrphansAsRoots() {
        List<Node> tree = TreeUtil.build(List.of(Node.of("root", null, 2), Node.of("child", "root", 3),
                Node.of("orphan", "missing", 1), Node.of("grandchild", "child", 4)),
                Node::id, Node::parentId, Node::withChildren);

        assertThat(tree).extracting(Node::id).containsExactly("root", "orphan");
        assertThat(tree.getFirst().children()).extracting(Node::id).containsExactly("child");
        assertThat(tree.getFirst().children().getFirst().children()).extracting(Node::id).containsExactly("grandchild");
        assertThat(tree.getLast().children()).isEmpty();
        assertThrows(UnsupportedOperationException.class, () -> tree.getFirst().children().add(Node.of("x", null, 0)));
    }

    @Test
    void traversalAndSortShouldKeepTreeOperationsPredictable() {
        Node root = Node.withChildren(Node.of("root", null, 3), List.of(Node.of("child-b", "root", 2),
                Node.of("child-a", "root", 1)));
        Node otherRoot = Node.of("other-root", null, 0);

        assertThat(TreeUtil.flatten(List.of(root, otherRoot), Node::children)).extracting(Node::id)
                .containsExactly("root", "child-b", "child-a", "other-root");
        assertThat(TreeUtil.postOrder(List.of(root, otherRoot), Node::children)).extracting(Node::id)
                .containsExactly("child-b", "child-a", "root", "other-root");
        assertThat(TreeUtil.levelOrder(List.of(root, otherRoot), Node::children)).extracting(Node::id)
                .containsExactly("root", "other-root", "child-b", "child-a");
        assertThat(TreeUtil.sort(List.of(root, otherRoot), Node::children, Comparator.comparing(Node::sortNo),
                Node::withChildren).getLast().children()).extracting(Node::id).containsExactly("child-a", "child-b");
    }

    @Test
    void buildShouldRejectInvalidIdsAndCycles() {
        IllegalArgumentException duplicateId = assertThrows(IllegalArgumentException.class, () -> TreeUtil.build(
                List.of(Node.of("same", null, 1), Node.of("same", null, 2)), Node::id, Node::parentId,
                Node::withChildren));
        assertThat(duplicateId.getMessage()).contains("重复");
        IllegalArgumentException emptyId = assertThrows(IllegalArgumentException.class, () -> TreeUtil.build(
                List.of(Node.of(null, null, 1)), Node::id, Node::parentId, Node::withChildren));
        assertThat(emptyId.getMessage()).contains("不能为空");
        IllegalArgumentException cycle = assertThrows(IllegalArgumentException.class, () -> TreeUtil.build(
                List.of(Node.of("a", "b", 1), Node.of("b", "a", 2)), Node::id, Node::parentId,
                Node::withChildren));
        assertThat(cycle.getMessage()).contains("循环");
    }

    private record Node(String id, String parentId, int sortNo, List<Node> children) {

        private Node {
            children = children == null ? List.of() : List.copyOf(children);
        }

        private static Node of(String id, String parentId, int sortNo) {
            return new Node(id, parentId, sortNo, List.of());
        }

        private static Node withChildren(Node node, List<Node> children) {
            return new Node(node.id, node.parentId, node.sortNo, children);
        }
    }
}
