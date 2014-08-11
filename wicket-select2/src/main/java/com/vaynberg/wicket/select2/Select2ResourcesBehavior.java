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

import org.apache.wicket.Application;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * Adds various resources needed by Select2 such as JavaScript and CSS. Which resources are added is controlled by the {@link ApplicationSettings}
 * object. Minified versions of JavaScript resources will be used when the application is configured in deployment mode.
 * 
 * @author igor
 * 
 */
public class Select2ResourcesBehavior extends AbstractBehavior {
	private static final long serialVersionUID = 1L;

	@Override
	public void renderHead(IHeaderResponse response) {

		final ApplicationSettings settings = ApplicationSettings.get();

		if (settings.isIncludeJquery()) {
			if (Application.DEPLOYMENT.equalsIgnoreCase(Application.get().getConfigurationType())) {
				response.renderJavascriptReference(settings.getJqueryMinifiedReference());
			} else {
				response.renderJavascriptReference(settings.getJqueryReference());
			}
		}

		if (settings.isIncludeMouseWheel()) {
			response.renderJavascriptReference(settings.getMouseWheelReference());
		}

		if (settings.isIncludeJavascript()) {
			if (Application.DEPLOYMENT.equalsIgnoreCase(Application.get().getConfigurationType())) {
				response.renderJavascriptReference(settings.getJavaScriptMinifiedReference());
			} else {
				response.renderJavascriptReference(settings.getJavaScriptReference());
			}
		}

		if (settings.isIncludeCss()) {
			response.renderCSSReference(settings.getCssReference());
		}
	}

}
