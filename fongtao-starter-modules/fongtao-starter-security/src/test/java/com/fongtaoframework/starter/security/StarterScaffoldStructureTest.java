package com.fongtaoframework.starter.security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.assertj.core.api.Assertions.assertThat;

class StarterScaffoldStructureTest {

    private static final List<String> EXPECTED_STARTER_MODULES = List.of(
            "fongtao-starter-dependencies",
            "fongtao-starter-parent",
            "fongtao-starter-core",
            "fongtao-starter-web",
            "fongtao-starter-mybatis",
            "fongtao-starter-security",
            "fongtao-starter-cache",
            "fongtao-starter-logging",
            "fongtao-starter-lock",
            "fongtao-starter-file",
            "fongtao-starter-forest",
            "fongtao-starter-observability");

    @Test
    void starterModulesPomShouldBeAggregatorOnlyAndStarterParentShouldManageBuildRules() throws Exception {
        Document starterModulesPom = readPom(projectRoot().resolve("pom.xml"));
        assertThat(directChildText(starterModulesPom, "artifactId")).isEqualTo("fongtao-starter-modules");
        assertThat(starterModulesPom.getDocumentElement().getElementsByTagName("parent").getLength()).isZero();
        assertThat(starterModulesPom.getDocumentElement().getElementsByTagName("dependencyManagement").getLength())
                .isZero();
        assertThat(starterModulesPom.getDocumentElement().getElementsByTagName("pluginManagement").getLength())
                .isZero();
        assertThat(directChildText(starterModulesPom, "description")).isEqualTo("基础 starter 聚合工程。");
        assertThat(moduleNames(starterModulesPom)).containsExactlyElementsOf(EXPECTED_STARTER_MODULES);

        Document dependenciesPom = readPom(projectRoot().resolve("fongtao-starter-dependencies/pom.xml"));
        assertThat(propertyText(dependenciesPom, "spring-boot.version")).isEqualTo("3.5.15");
        assertThat(dependenciesPom.getDocumentElement().getTextContent())
                .contains("spring-boot-dependencies")
                .contains("mybatis-plus.version")
                .contains("fongtao-starter-cache")
                .contains("fongtao-starter-logging")
                .contains("arthas.version");

        Document starterParentPom = readPom(projectRoot().resolve("fongtao-starter-parent/pom.xml"));
        assertThat(starterParentPom.getDocumentElement().getTextContent())
                .contains("fongtao-starter-dependencies")
                .contains("maven-compiler-plugin");
        assertThat(starterParentPom.getDocumentElement().getElementsByTagName("annotationProcessorPaths").getLength())
                .isEqualTo(1);
    }

    private Path projectRoot() {
        Path moduleDir = Path.of("").toAbsolutePath();
        return moduleDir.getParent();
    }

    private Document readPom(Path pomPath) throws Exception {
        assertThat(Files.exists(pomPath)).as("POM 不存在: %s", pomPath).isTrue();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        return factory.newDocumentBuilder().parse(pomPath.toFile());
    }

    private String directChildText(Document document, String tagName) {
        Element root = document.getDocumentElement();
        for (int index = 0; index < root.getChildNodes().getLength(); index++) {
            if (tagName.equals(root.getChildNodes().item(index).getNodeName())) {
                return root.getChildNodes().item(index).getTextContent();
            }
        }
        throw new IllegalArgumentException("POM 缺少直接子节点: " + tagName);
    }

    private String propertyText(Document document, String tagName) {
        return document.getDocumentElement().getElementsByTagName(tagName).item(0).getTextContent();
    }

    private List<String> moduleNames(Document document) {
        Element modules = (Element) document.getDocumentElement().getElementsByTagName("modules").item(0);
        return java.util.stream.IntStream.range(0, modules.getElementsByTagName("module").getLength())
                .mapToObj(index -> modules.getElementsByTagName("module").item(index).getTextContent())
                .toList();
    }
}
