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

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

/**
 * Adds drag & drop behavior to Select2MultiChoice components, i.e.
 * the list of selected values can be sorted.
 *
 * @author Tom Götz (tom@decoded.de)
 */
public class DragAndDropBehavior extends Behavior {

    @Override
    public void renderHead(Component component, IHeaderResponse response) {

        // Include jquery-ui reference, if configured
        final ApplicationSettings settings = ApplicationSettings.get();
        if (settings.isIncludeJqueryUI()) {
            response.render(JavaScriptHeaderItem.forReference(settings.getJqueryUIReference()));
        }

        // Render script to enable sortable/drag-and-drop behavior
        String script = "$('#%1$s').select2('container').find('ul.select2-choices').sortable({" +
                "containment: 'parent'," +
                "start: function() { $('#%1$s').select2('onSortStart'); }," +
                "update: function() { $('#%1$s').select2('onSortEnd'); }" +
                "});";
        response.render(OnDomReadyHeaderItem.forScript(JQuery.execute(script, component.getMarkupId())));
    }
}
