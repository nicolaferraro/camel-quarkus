/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.core;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.Registry;
import org.apache.camel.util.ObjectHelper;

/**
 * Producers of beans that are injectable via CDI.
 */
@Singleton
public class CamelProducers {
    private volatile CamelContext context;
    private volatile CamelRuntime runtime;

    public void setContext(CamelContext context) {
        this.context = context;
    }

    public void setRuntime(CamelRuntime runtime) {
        this.runtime = runtime;
    }

    @Singleton
    @Produces
    CamelContext camelContext() {
        return this.context;
    }

    @Singleton
    @Produces
    Registry camelRegistry() {
        return this.context.getRegistry();
    }

    @Singleton
    @Produces
    CamelRuntime camelRuntime() {
        return this.runtime;
    }

    @Produces
    ProducerTemplate camelProducerTemplate(InjectionPoint injectionPoint) {
        final ProducerTemplate template = this.context.createProducerTemplate();
        final Produce produce = injectionPoint.getAnnotated().getAnnotation(Produce.class);

        if (ObjectHelper.isNotEmpty(produce) && ObjectHelper.isNotEmpty(produce.value())) {
            template.setDefaultEndpointUri(produce.value());
        }

        return template;
    }

    @Produces
    FluentProducerTemplate camelFluentProducerTemplate(InjectionPoint injectionPoint) {
        final FluentProducerTemplate template = this.context.createFluentProducerTemplate();
        final Produce produce = injectionPoint.getAnnotated().getAnnotation(Produce.class);

        if (ObjectHelper.isNotEmpty(produce) && ObjectHelper.isNotEmpty(produce.value())) {
            template.setDefaultEndpointUri(produce.value());
        }

        return template;
    }

    @Produces
    ConsumerTemplate camelConsumerTemplate() {
        return this.context.createConsumerTemplate();
    }
}
