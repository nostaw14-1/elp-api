package com.elpdeliveries.api.controller;

import com.elpdeliveries.api.entity.Settings;
import com.elpdeliveries.api.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {
    private final SettingsService service;

    @GetMapping
    public Settings getSettings() {
        return service.getSettings();
    }

    @PutMapping
    public Settings updateSettings(@RequestBody Settings settings) {
        return service.save(settings);
    }
}
