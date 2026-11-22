package CatanSpring.catan.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PartidaRequest(@NotNull @Size(min = 2, max = 4) List<@NotBlank String> jugadors,  @Min(3) Integer puntsPerGuanyar) {}
