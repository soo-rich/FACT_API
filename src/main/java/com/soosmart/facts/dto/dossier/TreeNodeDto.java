package com.soosmart.facts.dto.dossier;

import com.soosmart.facts.enumpack.TreeNodeType;

import java.util.List;
import java.util.UUID;

public record TreeNodeDto(
        UUID id,
        TreeNodeType type,
        String numero,
        String reference,
        Boolean adopt,
        List<TreeNodeDto> children
) {
}
