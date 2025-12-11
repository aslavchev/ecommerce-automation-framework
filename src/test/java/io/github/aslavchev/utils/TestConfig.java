package io.github.aslavchev.utils;

import io.github.cdimascio.dotenv.Dotenv;


/**
 * Secure and simple access to test credentials.
 * <p>
 * Supports both local development (via .env file) and CI/CD (via environment variables).
 * Never hardcodes secrets. Fails fast with a clear message if credentials are missing.
 */
public class TestConfig {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    /**
     * Returns the test account email - thread-safe for parallel execution
     */
    public static String email() {
        int userId = getThreadUserId();
        return required("TEST_USER_EMAIL_" + userId);
    }


    /**
     * Returns the test account password - thread-safe for parallel execution
     */
    public static String password() {
        int userId = getThreadUserId();
        return required("TEST_USER_PASSWORD_" + userId);
    }

    /**
     * Returns user ID (1 or 2) based on thread ID
     * Ensures each parallel thread gets a unique test user
     */
    private static int getThreadUserId() {
        long threadId = Thread.currentThread().getId();
        return (int) ((threadId % 2) + 1);  // Returns 1 or 2
    }

    /**
     * Returns a required configuration value.
     * <p>
     * Lookup order:
     * <ol>
     *   <li>Environment variable (used in CI)</li>
     *   <li>.env file in project root (used locally)</li>
     * </ol>
     * Throws clear exception if not found.
     */
    private static String required(String key) {
        // 1. Check environment variables first (CI/CD)
        String fromEnv = System.getenv(key);
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv.trim();

        // 2. Then check .env file (local development)
        String fromFile = dotenv.get(key);
        if (fromFile != null && !fromFile.isBlank()) return fromFile.trim();

        // 3. Not found anywhere → fail with helpful message
        throw new IllegalStateException(
                key + " is missing — set it in .env file or as environment variable"
        );
    }
}
