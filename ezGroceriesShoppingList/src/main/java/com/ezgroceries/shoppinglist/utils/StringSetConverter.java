package com.ezgroceries.shoppinglist.utils;

import org.springframework.util.CollectionUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class StringSetConverter implements AttributeConverter<Collection<String>, String> {

        @Override
        public String convertToDatabaseColumn(Collection<String> set) {
            if (!CollectionUtils.isEmpty(set)) {
                return "," + String.join(",", set) + ",";
            } else {
                return null;
            }
        }

        @Override
        public Collection<String> convertToEntityAttribute(String joined) {
            if (joined != null) {
                String values = joined.substring(1, joined.length() - 1); //Removes leading and trailing commas
                return new HashSet<>(Arrays.asList(values.split(",")));
            }
            return new HashSet<>();
        }
    }