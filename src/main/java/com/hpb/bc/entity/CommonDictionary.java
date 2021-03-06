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

package com.hpb.bc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CommonDictionary extends BaseEntity {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.id
     *
     * @mbg.generated
     */
    @JsonIgnore
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.code
     *
     * @mbg.generated
     */
    private String code;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.label
     *
     * @mbg.generated
     */
    private String label;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.group_name
     *
     * @mbg.generated
     */
    @JsonIgnore
    private String groupName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.group_description
     *
     * @mbg.generated
     */
    @JsonIgnore
    private String groupDescription;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.display_index
     *
     * @mbg.generated
     */
    @JsonIgnore
    private Integer displayIndex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.parent_code
     *
     * @mbg.generated
     */
    @JsonIgnore
    private String parentCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.resource_key
     *
     * @mbg.generated
     */
    @JsonIgnore
    private String resourceKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.is_visible
     *
     * @mbg.generated
     */
    @JsonIgnore
    private Boolean isVisible;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column common_dictionary.is_predefined
     *
     * @mbg.generated
     */
    @JsonIgnore
    private Boolean isPredefined;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.id
     *
     * @return the value of common_dictionary.id
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.id
     *
     * @param id the value for common_dictionary.id
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.code
     *
     * @return the value of common_dictionary.code
     * @mbg.generated
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.code
     *
     * @param code the value for common_dictionary.code
     * @mbg.generated
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.label
     *
     * @return the value of common_dictionary.label
     * @mbg.generated
     */
    public String getLabel() {
        return label;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.label
     *
     * @param label the value for common_dictionary.label
     * @mbg.generated
     */
    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.group_name
     *
     * @return the value of common_dictionary.group_name
     * @mbg.generated
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.group_name
     *
     * @param groupName the value for common_dictionary.group_name
     * @mbg.generated
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.group_description
     *
     * @return the value of common_dictionary.group_description
     * @mbg.generated
     */
    public String getGroupDescription() {
        return groupDescription;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.group_description
     *
     * @param groupDescription the value for common_dictionary.group_description
     * @mbg.generated
     */
    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription == null ? null : groupDescription.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.display_index
     *
     * @return the value of common_dictionary.display_index
     * @mbg.generated
     */
    public Integer getDisplayIndex() {
        return displayIndex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.display_index
     *
     * @param displayIndex the value for common_dictionary.display_index
     * @mbg.generated
     */
    public void setDisplayIndex(Integer displayIndex) {
        this.displayIndex = displayIndex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.parent_code
     *
     * @return the value of common_dictionary.parent_code
     * @mbg.generated
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.parent_code
     *
     * @param parentCode the value for common_dictionary.parent_code
     * @mbg.generated
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.resource_key
     *
     * @return the value of common_dictionary.resource_key
     * @mbg.generated
     */
    public String getResourceKey() {
        return resourceKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.resource_key
     *
     * @param resourceKey the value for common_dictionary.resource_key
     * @mbg.generated
     */
    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey == null ? null : resourceKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.is_visible
     *
     * @return the value of common_dictionary.is_visible
     * @mbg.generated
     */
    public Boolean getIsVisible() {
        return isVisible;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.is_visible
     *
     * @param isVisible the value for common_dictionary.is_visible
     * @mbg.generated
     */
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column common_dictionary.is_predefined
     *
     * @return the value of common_dictionary.is_predefined
     * @mbg.generated
     */
    public Boolean getIsPredefined() {
        return isPredefined;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column common_dictionary.is_predefined
     *
     * @param isPredefined the value for common_dictionary.is_predefined
     * @mbg.generated
     */
    public void setIsPredefined(Boolean isPredefined) {
        this.isPredefined = isPredefined;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table common_dictionary
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CommonDictionary other = (CommonDictionary) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
                && (this.getLabel() == null ? other.getLabel() == null : this.getLabel().equals(other.getLabel()))
                && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
                && (this.getGroupDescription() == null ? other.getGroupDescription() == null : this.getGroupDescription().equals(other.getGroupDescription()))
                && (this.getDisplayIndex() == null ? other.getDisplayIndex() == null : this.getDisplayIndex().equals(other.getDisplayIndex()))
                && (this.getParentCode() == null ? other.getParentCode() == null : this.getParentCode().equals(other.getParentCode()))
                && (this.getResourceKey() == null ? other.getResourceKey() == null : this.getResourceKey().equals(other.getResourceKey()))
                && (this.getIsVisible() == null ? other.getIsVisible() == null : this.getIsVisible().equals(other.getIsVisible()))
                && (this.getIsPredefined() == null ? other.getIsPredefined() == null : this.getIsPredefined().equals(other.getIsPredefined()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table common_dictionary
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getLabel() == null) ? 0 : getLabel().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getGroupDescription() == null) ? 0 : getGroupDescription().hashCode());
        result = prime * result + ((getDisplayIndex() == null) ? 0 : getDisplayIndex().hashCode());
        result = prime * result + ((getParentCode() == null) ? 0 : getParentCode().hashCode());
        result = prime * result + ((getResourceKey() == null) ? 0 : getResourceKey().hashCode());
        result = prime * result + ((getIsVisible() == null) ? 0 : getIsVisible().hashCode());
        result = prime * result + ((getIsPredefined() == null) ? 0 : getIsPredefined().hashCode());
        return result;
    }
}