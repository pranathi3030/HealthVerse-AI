package HealthVerse_backend.controller;

import HealthVerse_backend.model.HealthProfile;
import HealthVerse_backend.repository.HealthProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/health")
public class HealthProfileController {

    private final HealthProfileRepository healthProfileRepository;

    public HealthProfileController(HealthProfileRepository healthProfileRepository) {
        this.healthProfileRepository = healthProfileRepository;
    }

    // Create Health Profile
    @PostMapping("/profile")
    public ResponseEntity<HealthProfile> createProfile(
            @RequestBody HealthProfile profile) {

        if (profile.getHeight() != null &&
            profile.getWeight() != null &&
            profile.getHeight() > 0) {

            double heightInMeters = profile.getHeight() / 100.0;

            double bmi = profile.getWeight()
                    / (heightInMeters * heightInMeters);

            profile.setBmi(Math.round(bmi * 100.0) / 100.0);
        }

        HealthProfile savedProfile =
                healthProfileRepository.save(profile);

        return ResponseEntity.ok(savedProfile);
    }

    // Get Health Profile
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfile(
            @PathVariable Long userId) {

        Optional<HealthProfile> profile =
                healthProfileRepository.findByUserId(userId);

        if (profile.isPresent()) {
            return ResponseEntity.ok(profile.get());
        }

        return ResponseEntity.notFound().build();
    }

    // Update Health Profile
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody HealthProfile updatedProfile) {

        Optional<HealthProfile> existing =
                healthProfileRepository.findByUserId(userId);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HealthProfile profile = existing.get();

        profile.setAge(updatedProfile.getAge());
        profile.setGender(updatedProfile.getGender());
        profile.setHeight(updatedProfile.getHeight());
        profile.setWeight(updatedProfile.getWeight());
        profile.setLifestyle(updatedProfile.getLifestyle());
        profile.setGoals(updatedProfile.getGoals());
        profile.setAllergies(updatedProfile.getAllergies());
        profile.setConditions(updatedProfile.getConditions());

        if (profile.getHeight() != null &&
            profile.getWeight() != null &&
            profile.getHeight() > 0) {

            double heightInMeters =
                    profile.getHeight() / 100.0;

            double bmi = profile.getWeight()
                    / (heightInMeters * heightInMeters);

            profile.setBmi(Math.round(bmi * 100.0) / 100.0);
        }

        return ResponseEntity.ok(
                healthProfileRepository.save(profile)
        );
    }

    // BMI Calculation
    @GetMapping("/bmi/{userId}")
    public ResponseEntity<?> calculateBMI(
            @PathVariable Long userId) {

        Optional<HealthProfile> profile =
                healthProfileRepository.findByUserId(userId);

        if (profile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HealthProfile healthProfile = profile.get();

        if (healthProfile.getHeight() == null ||
            healthProfile.getWeight() == null ||
            healthProfile.getHeight() <= 0) {

            return ResponseEntity.badRequest()
                    .body("Height and weight are required");
        }

        double heightInMeters =
                healthProfile.getHeight() / 100.0;

        double bmi =
                healthProfile.getWeight()
                        / (heightInMeters * heightInMeters);

        bmi = Math.round(bmi * 100.0) / 100.0;

        healthProfile.setBmi(bmi);

        healthProfileRepository.save(healthProfile);

        return ResponseEntity.ok(
                "BMI = " + bmi
        );
    }
}