package mayfly.core.validation;

import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;
import mayfly.core.validation.annotation.Valid;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ValidationHandlerTest {

    static class User{
        @NotBlank
        private String username;
    }

    static class Product{
        @NotBlank
        private String name;

        @NotNull
        @Valid
        private User[] us;

        @Valid
        private List<Integer> ids;

        @Valid
        private int[] is;
    }

    @Test
    public void validate() {
        Product p = new Product(); p.name = "111";
        User u = new User(); u.username = "111";
        User u2 = new User(); u2.username = "1";
        p.us = new User[]{u, u2};
        p.ids = Arrays.asList(1, 2);
        p.is = new int[]{3, 4};
        try {
            ValidationHandler.getInstance().validate(p);
        } catch (ParamValidErrorException e) {
            System.out.println(e.getMessage());
        }
    }
}