/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.mapper;

import com.hpb.bc.entity.AddressErcHolder;
import com.hpb.bc.entity.ContractErcStandardInfo;
import com.hpb.bc.example.AddressErcHolderExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AddressErcHolderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table address_erc_holder
     *
     * @mbg.generated
     */
    long countByExample(AddressErcHolderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table address_erc_holder
     *
     * @mbg.generated
     */
    int deleteByExample(AddressErcHolderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table address_erc_holder
     *
     * @mbg.generated
     */
    int insert(AddressErcHolder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table address_erc_holder
     *
     * @mbg.generated
     */
    int insertSelective(AddressErcHolder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table address_erc_holder
     *
     * @mbg.generated
     */
    List<AddressErcHolder> selectByExample(AddressErcHolderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table address_erc_holder
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") AddressErcHolder record, @Param("example") AddressErcHolderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table address_erc_holder
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") AddressErcHolder record, @Param("example") AddressErcHolderExample example);


    int selectHoldersCountByContractAddress(@Param("contractAddress") String contractAddress);

    List<ContractErcStandardInfo> selectHoldersCountByContractAddressList(@Param("list") List<String> contractAddressList);
}