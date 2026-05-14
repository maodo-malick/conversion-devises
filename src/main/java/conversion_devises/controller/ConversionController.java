package conversion_devises.controller;

import conversion_devises.dto.request.ConversionRequest;
import conversion_devises.dto.response.ConversionResponse;
import conversion_devises.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/convert")
@RequiredArgsConstructor
public class ConversionController {
    private final ConversionService conversionService;
    @PostMapping
    public ResponseEntity<ConversionResponse>convert(@RequestBody ConversionRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails )
    {
        return ResponseEntity.ok(conversionService.convert(request,userDetails.getUsername()));
    }

}
