package com.ryantenney.metrics.spring.reporter;

import static com.ryantenney.metrics.spring.reporter.LibratoReporterFactoryBean.*;

import java.util.concurrent.TimeUnit;

public class LibratoReporterElementParser extends AbstractReporterElementParser {

	private static final String DURATION_STRING_REGEX = "^\\d+\\s?(ns|us|ms|s|m|h|d)?$";

	@Override
	public String getType() {
		return "librato";
	}

	@Override
	protected Class<?> getBeanClass() {
		return LibratoReporterFactoryBean.class;
	}

	@Override
	protected void validate(ValidationContext c) {
		c.require(USERNAME);
		c.require(TOKEN);
		c.require(SOURCE);
		c.require(PERIOD, DURATION_STRING_REGEX, "Period is required and must be in the form '\\d+(ns|us|ms|s|m|h|d)'");

		c.optional(TIMEOUT, DURATION_STRING_REGEX, "Timeout must be in the form '\\d+(ns|us|ms|s|m|h|d)'");
		c.optional(NAME);
		c.optional(SANITIZER_REF);
		c.optional(HTTP_POSTER_REF);
		c.optional(PREFIX);
		c.optional(PREFIX_DELIMITER);
		c.optional(CLOCK_REF);

		c.optional(EXPANSION_CONFIG);
		c.optional(EXPANSION_CONFIG_REF);
		if (c.has(EXPANSION_CONFIG) && c.has(EXPANSION_CONFIG_REF)) {
			c.reject(FILTER_REF, "Librato Reporter element must not specify both the 'expansion-config' and 'expansion-config-ref' attributes");
		}

		if (c.optional(RATE_UNIT)) {
			TimeUnit.valueOf(c.get(RATE_UNIT));
		}
		if (c.optional(DURATION_UNIT)) {
			TimeUnit.valueOf(c.get(DURATION_UNIT));
		}

		c.optional(FILTER_PATTERN);
		c.optional(FILTER_REF);
		if (c.has(FILTER_PATTERN) && c.has(FILTER_REF)) {
			c.reject(FILTER_REF, "Reporter element must not specify both the 'filter' and 'filter-ref' attributes");
		}

		c.rejectUnmatchedProperties();
	}

}
