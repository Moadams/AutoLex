package main.java.controller;

import main.java.model.DataEntry;

import java.util.*;

public class DataManager {
    private final Map<String, DataEntry> entries = new HashMap<>();

    public void addEntry(DataEntry entry) {
        entries.put(entry.getId(), entry);
    }

    public void updateEntry(DataEntry entry) {
        entries.put(entry.getId(), entry);
    }

    public void deleteEntry(String id) {
        entries.remove(id);
    }

    public Collection<DataEntry> getAllEntries() {
        return entries.values();
    }

    public Optional<DataEntry> getEntryById(String id) {
        return Optional.ofNullable(entries.get(id));
    }
}
