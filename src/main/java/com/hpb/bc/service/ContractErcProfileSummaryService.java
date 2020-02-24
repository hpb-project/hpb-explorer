package com.hpb.bc.service;

import com.hpb.bc.entity.ContractErcProfileSummary;
import com.hpb.bc.entity.ContractErcProfileSummaryApprove;
import com.hpb.bc.model.ContractApproveModel;
import com.hpb.bc.model.ContractErcProfileSummaryModel;

import java.util.List;

public interface ContractErcProfileSummaryService {
    boolean submitContractMessageHolders(ContractErcProfileSummaryModel contractErcProfileSummaryModel);

    ContractErcProfileSummary queryContractMessageHolders(String contactAddress);

    List<ContractErcProfileSummaryApprove> queryContractErcProfileSummaryApproveList(ContractApproveModel contractApproveModel);

    boolean approveContractErcProifleSummary( ContractApproveModel contractApproveModel);
}
