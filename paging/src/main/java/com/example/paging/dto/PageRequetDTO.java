package com.example.paging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PageRequetDTO {

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    private String keyword;
    private String type;

    private LocalDate from;

    private LocalDate to;


    public Pageable getPageable(String sort){
        return PageRequest.of(this.page -1, this.size, Sort.by(sort).descending());
    }

    private String link;

    public String getLink() {

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);


            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
            link = builder.toString();
        }

        return link;
    }


}
