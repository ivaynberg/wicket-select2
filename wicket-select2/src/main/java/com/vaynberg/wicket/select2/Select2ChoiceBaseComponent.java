package com.vaynberg.wicket.select2;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.Strings;

public abstract class Select2ChoiceBaseComponent<M> extends HiddenField<M> {

    private final Settings settings = new Settings();
    
    public Select2ChoiceBaseComponent(String id) {
	this(id, null);
    }

    public Select2ChoiceBaseComponent(String id, IModel<M> model) {
	super(id, model);
	add(new Select2ResourcesBehavior());
	setOutputMarkupId(true);
    }

    /**
     * @return Select2 settings for this component
     */
    public final Settings getSettings() {
	return settings;
    }
    
    /**
     * Gets the markup id that is safe to use in jQuery by escaping dots in the default {@link #getMarkup()}
     * 
     * @return markup id
     */
    protected String getJquerySafeMarkupId() {
	return getMarkupId().replace(".", "\\\\.");
    }

    /**
     * Escapes single quotes in localized strings to be used as JavaScript strings enclosed in single quotes
     *
     * @param key
     *          resource key for localized message
     * @return localized string with escaped single quotes
     */
    protected String getEscapedJsString(String key) {
    String value = getString(key);

    return Strings.replaceAll(value, "'", "\\'").toString();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
	super.renderHead(response);

	// initialize select2

	response.render(OnDomReadyHeaderItem.forScript(JQuery.execute("$('#%s').select2(%s);", getJquerySafeMarkupId(),
		settings.toJson())));

	// select current value

	renderInitializationScript(response);
    }


    @Override
    protected void onInitialize() {
	super.onInitialize();

	// configure the ajax callbacks
	AjaxSettings ajax = settings.getAjax(true);

	ajax.setData(String
		.format("function(term, page) { return { term: term, page:page, '%s':true, '%s':[window.location.protocol, '//', window.location.host, window.location.pathname].join('')}; }",
			WebRequest.PARAM_AJAX, WebRequest.PARAM_AJAX_BASE_URL));

	ajax.setResults("function(data, page) { return data; }");

    // configure the localized strings/renderers
    getSettings().setFormatNoMatches("function() { return '" + getEscapedJsString("noMatches") + "';}");
    getSettings().setFormatInputTooShort("function(input, min) { return min - input.length == 1 ? '" + getEscapedJsString("inputTooShortSingular") + "' : '" + getEscapedJsString("inputTooShortPlural") + "'.replace('{number}', min - input.length); }");
    getSettings().setFormatSelectionTooBig("function(limit) { return limit == 1 ? '" + getEscapedJsString("selectionTooBigSingular") + "' : '" + getEscapedJsString("selectionTooBigPlural") + "'.replace('{limit}', limit); }");
    getSettings().setFormatLoadMore("function() { return '" + getEscapedJsString("loadMore") + "';}");
    getSettings().setFormatSearching("function() { return '" + getEscapedJsString("searching") + "';}");
    }
    
    /**
     * Renders script used to initialize the value of Select2 after it is created so it matches the current model
     * object.
     * 
     * @param response
     *            header response
     */
    protected abstract void renderInitializationScript(IHeaderResponse response);
    
    @Override
    public void onEvent(IEvent<?> event) {
	super.onEvent(event);

	if (event.getPayload() instanceof AjaxRequestTarget) {

	    AjaxRequestTarget target = (AjaxRequestTarget) event.getPayload();

	    if (target.getComponents().contains(this)) {

		// if this component is being repainted by ajax, directly, we must destroy Select2 so it removes
		// its elements from DOM

		target.prependJavaScript(JQuery.execute("$('#%s').select2('destroy');", getJquerySafeMarkupId()));
	    }
	}
    }
    
}
