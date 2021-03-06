/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.cloud.cc.api.manageusers;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.trustedanalytics.cloud.cc.api.CcMetadata;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CcOrgUser {

    private CcMetadata metadata;
    private CcOrgUserEntity entity;

    public CcOrgUser() {
    }

    public CcOrgUser(UUID guid, String username, String role) {
        CcMetadata meta = new CcMetadata();
        meta.setGuid(guid);
        metadata = meta;
        CcOrgUserEntity ent = new CcOrgUserEntity();
        ent.setUsername(username);
        List<String> roleList = new ArrayList<>();
        roleList.add(role);
        ent.setRoles(roleList);
        entity = ent;
    }

    public CcMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CcMetadata metadata) {
        this.metadata = metadata;
    }

    public CcOrgUserEntity getEntity() {
        return entity;
    }

    public void setEntity(CcOrgUserEntity entity) {
        this.entity = entity;
    }

    @JsonIgnore
    public UUID getGuid() {
        Optional<CcOrgUser> user = Optional.of(this);
        return user.map(CcOrgUser::getMetadata).map(CcMetadata::getGuid).orElse(null);
    }

    @JsonIgnore
    public String getUsername() {
        Optional<CcOrgUser> user = Optional.of(this);
        return user.map(CcOrgUser::getEntity).map(CcOrgUserEntity::getUsername).orElse(null);
    }

    @JsonIgnore
    public List<Role> getRoles() {
        Optional<CcOrgUser> user = Optional.of(this);
        return Stream.of(user)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(CcOrgUser::getEntity)
                .filter(e -> e.getRoles() != null)
                .flatMap(e -> e.getRoles().stream())
                .map(Role::getRoleByName)
                .collect(toList());
    }
}
