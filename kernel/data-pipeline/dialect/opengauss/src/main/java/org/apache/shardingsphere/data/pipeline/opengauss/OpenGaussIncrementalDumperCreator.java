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

package org.apache.shardingsphere.data.pipeline.opengauss;

import org.apache.shardingsphere.data.pipeline.api.config.ingest.DumperConfiguration;
import org.apache.shardingsphere.data.pipeline.api.ingest.channel.PipelineChannel;
import org.apache.shardingsphere.data.pipeline.api.ingest.dumper.IncrementalDumper;
import org.apache.shardingsphere.data.pipeline.api.ingest.position.IngestPosition;
import org.apache.shardingsphere.data.pipeline.api.metadata.loader.PipelineTableMetaDataLoader;
import org.apache.shardingsphere.data.pipeline.opengauss.ingest.OpenGaussWalDumper;
import org.apache.shardingsphere.data.pipeline.postgresql.ingest.wal.WalPosition;
import org.apache.shardingsphere.data.pipeline.spi.ingest.dumper.IncrementalDumperCreator;

/**
 * OpenGauss incremental dumper creator.
 */
public final class OpenGaussIncrementalDumperCreator implements IncrementalDumperCreator<WalPosition> {
    
    @Override
    public IncrementalDumper createIncrementalDumper(final DumperConfiguration dumperConfig, final IngestPosition<WalPosition> position,
                                                     final PipelineChannel channel, final PipelineTableMetaDataLoader metaDataLoader) {
        return new OpenGaussWalDumper(dumperConfig, position, channel, metaDataLoader);
    }
    
    @Override
    public String getType() {
        return "openGauss";
    }
}