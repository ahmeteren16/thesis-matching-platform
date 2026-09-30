package builder;

import com.lorem_ipsum.thesis.domain.BetreuerProfile;

import java.util.List;



public class BetreuerProfileBuilder {

    private String githubId = "ahmeteren22";
    private String name = "Ahmet Eren";
    private String email = "aaa@aaa";
    private List<String> tags = List.of("tag");



    public static BetreuerProfileBuilder aBetreuer() {
        return new BetreuerProfileBuilder();
    }

    public BetreuerProfileBuilder withGithubId(String githubId) {
        this.githubId = githubId;
        return this;
    }

    public BetreuerProfileBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BetreuerProfileBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BetreuerProfileBuilder withTags(List<String> tags) {
        this.tags = (tags == null) ? List.of() : List.copyOf(tags);
        return this;
    }

    public BetreuerProfile buildwithoutId() {
        return BetreuerProfile.createBetreuer(githubId, name, email, tags);
    }

    public BetreuerProfile buildwithId(Integer id) {
        return new BetreuerProfile(id,githubId, name, email, tags);
    }
}
