/*
 * Copyright 2012 Igor Vaynberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.vaynberg.wicket.select2;

import java.util.Collection;

/**
 * A simple {@code TextChoiceProvider} for Strings.
 *
 * @author Tom Götz (tom@decoded.de)
 */
public abstract class StringTextChoiceProvider extends TextChoiceProvider<String> {

    @Override
    protected String getDisplayText(String choice) {
        return choice;
    }

    @Override
    protected Object getId(String choice) {
        return choice;
    }

    @Override
    public Collection<String> toChoices(Collection<String> ids) {
        return ids;
    }
}
