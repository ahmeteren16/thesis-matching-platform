package com.lorem_ipsum.thesis.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ThemaAddRequest(@NotBlank @Size(min = 2, max = 50) String titel,
                              @Size(max = 50) String beschreibung,
                              List<@Size(max = 30) String> voraussetzungen) {
}
