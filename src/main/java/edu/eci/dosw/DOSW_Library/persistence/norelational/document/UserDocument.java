package edu.eci.dosw.DOSW_Library.persistence.norelational.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {

    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private String role;
    private String email;
    private String membershipType;
    private LocalDate registeredDate;
}