package com.soosmart.facts.service.dossier;

import com.soosmart.facts.dto.dossier.borderau.BorderauDto;

import java.util.List;
import java.util.UUID;

public interface BordereauService {
BorderauDto saveBordereau(UUID id_proforma);
List<BorderauDto> getBordereauAll();
BorderauDto getBordereau(UUID id);
void deleteBordereau(UUID id);
}
