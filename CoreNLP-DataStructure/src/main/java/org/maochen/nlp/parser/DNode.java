package org.maochen.nlp.parser;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Maochen.G   contact@maochen.org
 * License: check the LICENSE file.
 * <p>
 * This follows CoNLL-U shared task: Multi-Lingual Dependency Parsing Format
 * http://universaldependencies.github.io/docs/format.html
 * <p>
 * Created by Maochen on 12/8/14.
 */
public class DNode {
    private int id;
    private String form;
    private String lemma;
    private String cPOSTag;
    private String pos;
    private Map<String, String> feats = new HashMap<>();
    private DNode head;
    private String depLabel;
    // Still considering item 9 and 10.

    // Key - id
    private Map<Integer, DNode> children = new HashMap<>();
    // Parent Node, Semantic Head Label
    private Map<DNode, String> semanticHeads = new HashMap<>();
    private Set<DNode> semanticChildren = new HashSet<>();
    private DTree tree = null; // Refs to the whole dependency tree
    public static final String NAMED_ENTITY_KEY = "name_entity";

    public DNode() {
        id = 0;
        form = StringUtils.EMPTY;
        lemma = StringUtils.EMPTY;
        cPOSTag = StringUtils.EMPTY;
        pos = StringUtils.EMPTY;
        head = null;
        depLabel = StringUtils.EMPTY;
    }

    public DNode(int id, String form, String lemma, String cPOSTag, String pos, String depLabel) {
        this();
        this.id = id;
        this.form = form;
        this.lemma = lemma;
        this.cPOSTag = cPOSTag;
        this.pos = pos;
        this.depLabel = depLabel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getcPOSTag() {
        return cPOSTag;
    }

    public void setcPOSTag(String cPOSTag) {
        this.cPOSTag = cPOSTag;
    }

    public String getPOS() {
        return pos;
    }

    public void setPOS(String pos) {
        this.pos = pos;
    }

    public String getDepLabel() {
        return depLabel;
    }

    public void setDepLabel(String depLabel) {
        this.depLabel = depLabel;
    }

    public DNode getHead() {
        return head;
    }

    public void setHead(DNode head) {
        this.head = head;
    }

    public List<DNode> getChildren() {
        return children.values().stream().collect(Collectors.toList());
    }

    public void addChild(DNode child) {
        this.children.put(child.getId(), child);
    }

    public void removeChild(int id) {
        children.remove(id);
    }

    public void addFeature(String key, String value) {
        feats.put(key, value);
    }

    public String getFeature(String key) {
        return feats.get(key);
    }

    public void setFeats(Map<String, String> feats) {
        if (feats != null) {
            this.feats = feats;
        }
    }

    public Map<String, String> getFeats() {
        return feats;
    }

    public List<DNode> getChildrenByDepLabels(final String... labels) {
        return children.values().stream().parallel().filter(x -> Arrays.asList(labels).contains(x.getDepLabel())).collect(Collectors.toList());
    }

    public String getNamedEntity() {
        return feats.get(NAMED_ENTITY_KEY) == null ? StringUtils.EMPTY : feats.get(NAMED_ENTITY_KEY);
    }

    public void setNamedEntity(String namedEntity) {
        if (namedEntity != null) {
            feats.put(NAMED_ENTITY_KEY, namedEntity);
        }
    }

    public Set<DNode> getSemanticChildren() {
        return semanticChildren;
    }

    public void setSemanticChildren(Set<DNode> semanticChildren) {
        this.semanticChildren = semanticChildren;
    }

    public boolean isRoot() {
        return this.depLabel.equals(LangLib.DEP_ROOT);
    }

    public void addSemanticHead(DNode parent, String label) {
        semanticHeads.put(parent, label);
    }

    public Map<DNode, String> getSemanticHeads() {
        return semanticHeads;
    }

    public DTree getTree() {
        return tree;
    }

    public void setTree(DTree tree) {
        this.tree = tree;
    }

    // This is CoNLL-U format.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id).append("\t");
        builder.append(form).append("\t");
        builder.append(lemma).append("\t");

        // For now, cPOSTag is not differentiate with the POS.
        String cPOSTagString = cPOSTag.isEmpty() ? pos : cPOSTag;
        builder.append(cPOSTagString).append("\t");

        builder.append(pos).append("\t");

        String featsString = feats.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).reduce((s1, s2) -> s1 + "|" + s2).orElse("_");
        builder.append(featsString).append("\t");

        String headString = head == null ? "NULL" : String.valueOf(head.id);
        builder.append(headString).append("\t");

        builder.append(depLabel).append("\t");

        // These two corresponds to the 9 and 10 in the standard.
        builder.append("_").append("\t");
        builder.append("_").append("\t");

        // Add semantic role label in the last column semantichead=srl, e.g. 3=A0
        String semanticHeadsString = semanticHeads.entrySet().stream().map(e -> e.getKey().getId() + ":" + e.getValue()).reduce((s1, s2) -> s1 + "|" + s2).orElse("_");
        builder.append(semanticHeadsString).append("\t");

        return builder.toString().trim();
    }
}
