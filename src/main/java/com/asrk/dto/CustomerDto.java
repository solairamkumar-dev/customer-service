package com.asrk.dto;

import com.asrk.domain.Genre;

import java.util.List;

public record CustomerDto(Integer id, String name, Genre favoriteGenre, List<MovieDto> recommendedMovies) {
}
