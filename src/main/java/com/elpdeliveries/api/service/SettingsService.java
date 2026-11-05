package com.elpdeliveries.api.service;

import com.elpdeliveries.api.entity.Settings;
import com.elpdeliveries.api.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepository repository;
    public Settings getSettings() {
        return repository.findFirstByOrderByIdAsc().orElseGet(() -> {
            Settings settings = new Settings();
            return repository.save(settings);
        });
    }

    public Settings save(Settings updated) {
        Settings current = repository.findFirstByOrderByIdAsc().orElse(new Settings());
        current.setVroomIp(updated.getVroomIp());
        current.setDepotLat(updated.getDepotLat());
        current.setDepotLong(updated.getDepotLong());
        current.setAwsBucket(updated.getAwsBucket());
        current.setAwsAccessKey(updated.getAwsAccessKey());
        current.setAwsSecretKey(updated.getAwsSecretKey());
        current.setSqlServer(updated.getSqlServer());
        current.setSqlUsername(updated.getSqlUsername());
        current.setSqlPassword(updated.getSqlPassword());
        return repository.save(current);
    }
}
