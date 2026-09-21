package com.ai.x.engine.mapping;

import jakarta.servlet.Filter;

public class FilterMapping extends AbstractMapping{
    public final Filter filter;
    public final String filterName;

    public FilterMapping(String filterName, String urlPattern, Filter filter) {
        super(urlPattern);
        this.filterName = filterName;
        this.filter = filter;
    }
}
