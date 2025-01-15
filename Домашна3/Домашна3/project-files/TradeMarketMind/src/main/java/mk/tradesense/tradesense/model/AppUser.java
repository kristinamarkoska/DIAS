package mk.tradesense.tradesense.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserDetails userRole;

    public AppUser(String username, String email, String password, UserDetails userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    private boolean isExpired = false;
    private boolean isLocked = false;
    private boolean areCredentialsExpired = false;
    private boolean isActive = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> userRole.name());
    }


    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !areCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
