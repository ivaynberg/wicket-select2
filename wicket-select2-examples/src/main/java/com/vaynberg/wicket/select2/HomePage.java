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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

/**
 * Example page.
 *
 * @author igor
 */
@SuppressWarnings("unused")
public class HomePage extends WebPage {
    private static final int PAGE_SIZE = 20;

    private Country country = Country.US;
    private List<Country> countries = new ArrayList<Country>(Arrays.asList(new Country[]{Country.US, Country.CA}));
    private List<TimeZone> timeZones = new ArrayList<TimeZone>();

    public HomePage() {

        // single-select example

        add(new Label("country", new PropertyModel<Object>(this, "country")));

        Form<?> form = new Form<Void>("single");
        add(form);

        Select2Choice<Country> country = new Select2Choice<Country>("country", new PropertyModel<Country>(this,
                "country"), new CountriesProvider());
        country.getSettings().setMinimumInputLength(1);
        form.add(country);

        // multi-select example

        add(new Label("countries", new PropertyModel<Object>(this, "countries")));

        Form<?> multi = new Form<Void>("multi");
        add(multi);

        Select2MultiChoice<Country> countries = new Select2MultiChoice<Country>("countries",
                new PropertyModel<Collection<Country>>(this, "countries"), new CountriesProvider());
        countries.getSettings().setMinimumInputLength(1);
        countries.add(new DragAndDropBehavior());
        multi.add(countries);


        // grouped-multi-select example

        add(new Label("timezones", new PropertyModel<Object>(this, "timeZones")));
        Form<?> groupedMulti = new Form<Void>("groupedmulti");
        add(groupedMulti);

        Select2MultiChoice<TimeZone> timeZones = new Select2GroupedMultiChoice<TimeZone>("timezones",
                new PropertyModel<Collection<TimeZone>>(this, "timeZones"), new TimeZoneProvider());
        timeZones.add(new DragAndDropBehavior());
        groupedMulti.add(timeZones);
    }

    /**
     * Queries {@code pageSize} worth of countries from the {@link Country} enum, starting with {@code page * pageSize}
     * offset. Countries are matched on their {@code displayName} containing {@code term}
     *
     * @param term     search term
     * @param page     starting page
     * @param pageSize items per page
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
     */
    public class CountriesProvider extends TextChoiceProvider<Country> {

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

    private final Map<String, TimeZone> groupedStates = new HashMap<String, TimeZone>() {{
        put("AK", new TimeZone("Alaska", "AK", "Alaskan/Hawaiian"));
        put("HI", new TimeZone("Hawaii", "HI", "Alaskan/Hawaiian"));
        put("AZ", new TimeZone("Arizona", "AZ", "Mountain"));
        put("CO", new TimeZone("Colorado", "CO", "Mountain"));
        put("CA", new TimeZone("California", "CA", "Pacific"));
        put("OR", new TimeZone("Oregon", "OR", "Pacific"));
        put("WA", new TimeZone("Washington", "WA", "Pacific"));
        put("UN1", new TimeZone("Ungrouped 1", "UN1", null));
        put("UN2", new TimeZone("Ungrouped 2", "UN2", null));
    }};

    private class TimeZoneProvider extends TextChoiceProvider<TimeZone> {
        @Override
        protected String getDisplayText(TimeZone choice) {
            return choice.getDisplayText();
        }

        @Override
        protected Object getId(TimeZone choice) {
            return choice.getId();
        }

        @Override
        public void query(String term, int page, Response<TimeZone> response) {
            final ArrayList<TimeZone> timezones = new ArrayList<TimeZone>();
            for (TimeZone tz : groupedStates.values()) {
                if (tz.getDisplayText().toLowerCase().contains(term.toLowerCase())) {
                    timezones.add(tz);
                }
            }
            response.addAll(timezones);
            response.setHasMore(response.size() == 10);
        }

        @Override
        public Collection<TimeZone> toChoices(Collection<String> ids) {
            final ArrayList<TimeZone> timeZones = new ArrayList<TimeZone>();
            for (String id : ids) {
                timeZones.add(groupedStates.get(id));
            }
            return timeZones;
        }
    }
}
