package com.hpb.bc.contracts;

import java.math.BigInteger;

public class HpbBallotVote {
    private String voterAddr;
    private String candidateAddr;
    private BigInteger num;

    public String getVoterAddr() {
        return voterAddr;
    }

    public void setVoterAddr(String voterAddr) {
        this.voterAddr = voterAddr;
    }

    public String getCandidateAddr() {
        return candidateAddr;
    }

    public void setCandidateAddr(String candidateAddr) {
        this.candidateAddr = candidateAddr;
    }

    public BigInteger getNum() {
        return num;
    }

    public void setNum(BigInteger num) {
        this.num = num;
    }

    public HpbBallotVote(String voterAddr, String candidateAddr, BigInteger num) {
        super();
        this.voterAddr = voterAddr;
        this.candidateAddr = candidateAddr;
        this.num = num;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((candidateAddr == null) ? 0 : candidateAddr.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((voterAddr == null) ? 0 : voterAddr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HpbBallotVote other = (HpbBallotVote) obj;
        if (candidateAddr == null) {
            if (other.candidateAddr != null)
                return false;
        } else if (!candidateAddr.equals(other.candidateAddr))
            return false;
        if (num == null) {
            if (other.num != null)
                return false;
        } else if (!num.equals(other.num))
            return false;
        if (voterAddr == null) {
            if (other.voterAddr != null)
                return false;
        } else if (!voterAddr.equals(other.voterAddr))
            return false;
        return true;
    }
}
