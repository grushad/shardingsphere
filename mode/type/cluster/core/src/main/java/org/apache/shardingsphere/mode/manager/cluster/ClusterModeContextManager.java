/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.mode.manager.cluster;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.infra.datasource.props.DataSourceProperties;
import org.apache.shardingsphere.infra.instance.mode.ModeContextManager;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.mode.manager.ContextManagerAware;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Cluster mode context manager.
 */
@RequiredArgsConstructor
public final class ClusterModeContextManager implements ModeContextManager, ContextManagerAware {
    
    private ContextManager contextManager;
    
    @Override
    public void createDatabase(final String databaseName) {
        contextManager.getMetaDataContexts().getPersistService().getDatabaseMetaDataService().addDatabase(databaseName);
    }
    
    @Override
    public void dropDatabase(final String databaseName) {
        contextManager.getMetaDataContexts().getPersistService().getDatabaseMetaDataService().dropDatabase(databaseName);
    }
    
    @Override
    public void registerStorageUnits(final String databaseName, final Map<String, DataSourceProperties> toBeRegisterStorageUnitProps) {
        contextManager.getMetaDataContexts().getPersistService().getDataSourceService().append(databaseName, toBeRegisterStorageUnitProps);
    }
    
    @Override
    public void alterStorageUnits(final String databaseName, final Map<String, DataSourceProperties> toBeUpdatedStorageUnitProps) {
        contextManager.getMetaDataContexts().getPersistService().getDataSourceService().append(databaseName, toBeUpdatedStorageUnitProps);
    }
    
    @Override
    public void unregisterStorageUnits(final String databaseName, final Collection<String> toBeDroppedStorageUnitNames) {
        contextManager.getMetaDataContexts().getPersistService().getDataSourceService().persist(databaseName,
                getToBeReversedDataSourcePropsMap(contextManager.getMetaDataContexts().getPersistService().getDataSourceService().load(databaseName), toBeDroppedStorageUnitNames));
    }
    
    private Map<String, DataSourceProperties> getToBeReversedDataSourcePropsMap(final Map<String, DataSourceProperties> dataSourcePropsMap, final Collection<String> toBeDroppedResourceNames) {
        return dataSourcePropsMap.entrySet().stream().filter(entry -> !toBeDroppedResourceNames.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    @Override
    public void alterRuleConfiguration(final String databaseName, final Collection<RuleConfiguration> ruleConfigs) {
        contextManager.getMetaDataContexts().getPersistService().getDatabaseRulePersistService().persist(databaseName, ruleConfigs);
    }
    
    @Override
    public void alterGlobalRuleConfiguration(final Collection<RuleConfiguration> globalRuleConfigs) {
        contextManager.getMetaDataContexts().getPersistService().getGlobalRuleService().persist(globalRuleConfigs);
    }
    
    @Override
    public void alterProperties(final Properties props) {
        contextManager.getMetaDataContexts().getPersistService().getPropsService().persist(props);
    }
    
    @Override
    public void setContextManagerAware(final ContextManager contextManager) {
        this.contextManager = contextManager;
    }
}