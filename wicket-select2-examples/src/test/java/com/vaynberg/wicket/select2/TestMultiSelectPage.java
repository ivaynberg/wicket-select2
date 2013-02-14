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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

/**
 * Example page.
 * 
 * @author igor
 * 
 */
@SuppressWarnings("unused")
public class TestMultiSelectPage extends WebPage {
    
    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 20;

    private Country country = Country.US;
    private List<Country> countries = new ArrayList<Country>(Arrays.asList(new Country[] { Country.US, Country.CA }));
    
    private Form<?> form;

    private Select2MultiChoice<Country> countriesChoice;
    
    public TestMultiSelectPage() {

	// single-select example


	form = new Form<Void>("multi");
	add(form);

	countriesChoice = new Select2MultiChoice<Country>("countries",
		new PropertyModel<Collection<Country>>(this, "countries"), new CountriesProvider());
	countriesChoice.getSettings().setMinimumInputLength(1);
	countriesChoice.add(new DragAndDropBehavior());
	form.add(countriesChoice);
	
    }

    /**
     * Queries {@code pageSize} worth of countries from the {@link Country} enum, starting with {@code page * pageSize}
     * offset. Countries are matched on their {@code displayName} containing {@code term}
     * 
     * @param term
     *            search term
     * @param page
     *            starting page
     * @param pageSize
     *            items per page
     * @return list of matches
     */
    private static List<Country> queryMatches(String term, int page, int pageSize) {

	List<Country> result = new ArrayList<Country>();

	term = term.toUpperCase();

	final int offset = page * pageSize;

	int matched = 0;
	for (Country country : Country.values()) {
	    if (result.size() == pageSize) {
		break;
	    }

	    if (country.getDisplayName().toUpperCase().contains(term)) {
		matched++;
		if (matched > offset) {
		    result.add(country);
		}
	    }
	}
	return result;
    }

    /**
     * {@link Country} based choice provider for Select2 components. Demonstrates integration between Select2 components
     * and a domain object (in this case an enum).
     * 
     * @author igor
     * 
     */
    public static class CountriesProvider extends TextChoiceProvider<Country> {

	@Override
	protected String getDisplayText(Country choice) {
	    return choice.getDisplayName();
	}

	@Override
	protected Object getId(Country choice) {
	    return choice.name();
	}

	@Override
	public void query(String term, int page, Response<Country> response) {
	    response.addAll(queryMatches(term, page, 10));
	    response.setHasMore(response.size() == 10);

	}

	@Override
	public Collection<Country> toChoices(Collection<String> ids) {
	    ArrayList<Country> countries = new ArrayList<Country>();
	    for (String id : ids) {
		countries.add(Country.valueOf(id));
	    }
	    return countries;
	}

    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public Form<?> getForm() {
        return form;
    }

    public void setForm(Form<?> form) {
        this.form = form;
    }

    public Select2MultiChoice<Country> getCountriesChoice() {
        return countriesChoice;
    }

    public void setCountriesChoice(Select2MultiChoice<Country> countriesChoice) {
        this.countriesChoice = countriesChoice;
    }

}
