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
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application-wide settings that apply to all Select2 components.
 * 
 * The settings object is retreived via the static {@link #get()} method and is usually configured in the {@link WebApplication#init()} method.
 * 
 * @author igor
 */
public class ApplicationSettings {
	private static final MetaDataKey<ApplicationSettings> KEY = new MetaDataKey<ApplicationSettings>() {
		private static final long serialVersionUID = 1L;
	};

	private ResourceReference javaScriptReference = new ResourceReference(ApplicationSettings.class, "res/select2.js");
	private ResourceReference javaScriptMinifiedReference = new ResourceReference(ApplicationSettings.class, "res/select2.min.js");
	private ResourceReference mouseWheelReference = new ResourceReference(ApplicationSettings.class, "res/jquery.mousewheel.js");
	private ResourceReference cssReference = new ResourceReference(ApplicationSettings.class, "res/select2.css");
	private ResourceReference jqueryReference = new ResourceReference(ApplicationSettings.class, "res/jquery-1.7.2.js");
	private ResourceReference jqueryMinifiedReference = new ResourceReference(ApplicationSettings.class, "res/jquery-1.7.2.min.js");

	private boolean includeJquery = true;
	private boolean includeMouseWheel = true;
	private boolean includeJavascript = true;
	private boolean includeCss = true;

	/**
	 * Private constructor, use {@link #get()} instead.
	 */
	private ApplicationSettings() {
	}

	public boolean isIncludeJquery() {
		return includeJquery;
	}

	public ApplicationSettings setIncludeJquery(boolean includeJquery) {
		this.includeJquery = includeJquery;
		return this;
	}

	public boolean isIncludeJavascript() {
		return includeJavascript;
	}

	public ApplicationSettings setIncludeJavascript(boolean includeJavascript) {
		this.includeJavascript = includeJavascript;
		return this;
	}

	public boolean isIncludeCss() {
		return includeCss;
	}

	public ApplicationSettings setIncludeCss(boolean includeCss) {
		this.includeCss = includeCss;
		return this;
	}

	public ResourceReference getJavaScriptReference() {
		return javaScriptReference;
	}

	public ApplicationSettings setJavaScriptReference(ResourceReference javaScriptReference) {
		this.javaScriptReference = javaScriptReference;
		return this;
	}

	public ResourceReference getCssReference() {
		return cssReference;
	}

	public ApplicationSettings setCssReference(ResourceReference cssReference) {
		this.cssReference = cssReference;
		return this;
	}

	public ResourceReference getJqueryReference() {
		return jqueryReference;
	}

	public ApplicationSettings setJqueryReference(ResourceReference jqueryReference) {
		this.jqueryReference = jqueryReference;
		return this;
	}

	public boolean isIncludeMouseWheel() {
		return includeMouseWheel;
	}

	public ApplicationSettings setIncludeMouseWheel(boolean includeJqueryMouseWheelPlugin) {
		this.includeMouseWheel = includeJqueryMouseWheelPlugin;
		return this;
	}

	public ResourceReference getMouseWheelReference() {
		return mouseWheelReference;
	}

	public ApplicationSettings setMouseWheelReference(ResourceReference mousewheelReference) {
		this.mouseWheelReference = mousewheelReference;
		return this;
	}

	public ResourceReference getJavaScriptMinifiedReference() {
		return javaScriptMinifiedReference;
	}

	public ApplicationSettings setJavaScriptMinifiedReference(ResourceReference javaScriptMinifiedReference) {
		this.javaScriptMinifiedReference = javaScriptMinifiedReference;
		return this;
	}

	public ResourceReference getJqueryMinifiedReference() {
		return jqueryMinifiedReference;
	}

	public ApplicationSettings setJqueryMinifiedReference(ResourceReference jqueryMinifiedReference) {
		this.jqueryMinifiedReference = jqueryMinifiedReference;
		return this;
	}

	/**
	 * Retrieves the instance of settings object.
	 * 
	 * @return settings instance
	 */
	public static ApplicationSettings get() {
		// FIXME Application should provide setMetadataIfAbsent()
		Application application = Application.get();
		ApplicationSettings settings = application.getMetaData(KEY);
		if (settings == null) {
			synchronized (application) {
				settings = application.getMetaData(KEY);
				if (settings == null) {
					settings = new ApplicationSettings();
					application.setMetaData(KEY, settings);
				}
			}
		}
		return application.getMetaData(KEY);
	}

}
