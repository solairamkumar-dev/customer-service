package com.asrk.dto;

import com.asrk.domain.Genre;

public record MovieDto (Integer id,
                       String title,
                       Integer releaseYear,
                       Genre genre){
}
