package esmukanov.ds.system.models;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Attachment {

    private String fileName;
    private byte[] fileContent;
    private String contentType;
}
