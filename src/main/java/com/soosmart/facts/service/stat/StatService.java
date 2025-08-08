package com.soosmart.facts.service.stat;

import com.soosmart.facts.dto.pagination.CustomPageResponse;
import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.dto.stat.Chart;
import com.soosmart.facts.dto.stat.FacturePaid;
import com.soosmart.facts.dto.stat.Statistique;
import com.soosmart.facts.dto.stat.Table;

import java.util.List;

public interface StatService {
    Statistique getStatistique() ;

    FacturePaid getTotalFacture();

    List<Chart> getChartData();

    CustomPageResponse<Table> ListDocument(PaginatedRequest paginatedRequest);

}
