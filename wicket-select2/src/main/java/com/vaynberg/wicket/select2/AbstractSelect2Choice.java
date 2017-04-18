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

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.wicket.IResourceListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.http.WebResponse;
import org.json.JSONException;
import org.json.JSONWriter;

/**
 * Base class for Select2 components
 * 
 * @author igor
 * 
 * @param <T>
 *            type of choice object
 * @param <M>
 *            type of model object
 */
abstract class AbstractSelect2Choice<T, M> extends Select2ChoiceBaseComponent<M> implements IResourceListener {

    private ChoiceProvider<T> provider;

    /**
     * Constructor
     * 
     * @param id
     *            component id
     */
    public AbstractSelect2Choice(String id) {
	this(id, null, null);
    }

    /**
     * Constructor
     * 
     * @param id
     *            component id
     * @param model
     *            component model
     */
    public AbstractSelect2Choice(String id, IModel<M> model) {
	this(id, model, null);
    }

    /**
     * Constructor.
     * 
     * @param id
     *            component id
     * @param provider
     *            choice provider
     */
    public AbstractSelect2Choice(String id, ChoiceProvider<T> provider) {
	this(id, null, provider);
    }

    /**
     * Constructor
     * 
     * @param id
     *            component id
     * @param model
     *            component model
     * @param provider
     *            choice provider
     */
    public AbstractSelect2Choice(String id, IModel<M> model, ChoiceProvider<T> provider) {
	super(id, model);
	this.provider = provider;
	
    }

   

    /**
     * Sets the choice provider
     * 
     * @param provider
     */
    public final void setProvider(ChoiceProvider<T> provider) {
	this.provider = provider;
    }

    /**
     * @return choice provider
     */
    public final ChoiceProvider<T> getProvider() {
	if (provider == null) {
	    throw new IllegalStateException("Select2 choice component: " + getId()
		    + " does not have a ChoiceProvider set");
	}
	return provider;
    }

    @Override
    protected void onConfigure() {
	super.onConfigure();

	getSettings().getAjax().setUrl(urlFor(IResourceListener.INTERFACE, null));
    }

   

    @Override
    public void onResourceRequested() {

	// this is the callback that retrieves matching choices used to populate the dropdown

	Request request = getRequestCycle().getRequest();
	IRequestParameters params = request.getRequestParameters();

	// retrieve choices matching the search term

	String term = params.getParameterValue("term").toOptionalString();

	int page = params.getParameterValue("page").toInt(1);
	// select2 uses 1-based paging, but in wicket world we are used to
	// 0-based
	page -= 1;

	Exception ex = null;
	Response<T> response = new Response<T>();
	try{
	provider.query(term, page, response);
	} catch (Exception e)
	{
	    ex = e;
	}
	// jsonize and write out the choices to the response

	WebResponse webResponse = (WebResponse) getRequestCycle().getResponse();
	webResponse.setContentType("application/json");

	OutputStreamWriter out = new OutputStreamWriter(webResponse.getOutputStream(), getRequest().getCharset());
	JSONWriter json = new JSONWriter(out);

	try {
	    json.object();
	    json.key("results").array();
	    for (T item : response) {
		json.object();
		provider.toJson(item, json);
		json.endObject();
	    }
	    json.endArray();
	    if (ex==null)
	    {
		json.key("more").value(response.getHasMore()).endObject();
	    }
	    else
	    {
		json.key("error").value(ex.getMessage());
		json.key("more").value(false).endObject();
	    }
	} catch (JSONException e) {
	    throw new RuntimeException("Could not write Json response", e);
	}

	try {
	    out.flush();
	} catch (IOException e) {
	    throw new RuntimeException("Could not write Json to servlet response", e);
	}
    }

    @Override
    protected void onDetach() {
    	provider.detach();
    	super.onDetach();
    }

    @Override
    protected boolean getStatelessHint() {
        return false;
    }
    
}
