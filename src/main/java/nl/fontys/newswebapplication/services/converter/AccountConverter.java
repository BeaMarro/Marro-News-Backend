package nl.fontys.newswebapplication.services.converter;

import lombok.AllArgsConstructor;
import nl.fontys.newswebapplication.domain.*;
import nl.fontys.newswebapplication.domain.enums.Department;
import nl.fontys.newswebapplication.domain.response.AccountResponse;
import nl.fontys.newswebapplication.domain.response.AdminResponse;
import nl.fontys.newswebapplication.domain.response.JournalistResponse;
import nl.fontys.newswebapplication.domain.response.UserResponse;
import nl.fontys.newswebapplication.repositories.entity.AccountEntity;
import nl.fontys.newswebapplication.repositories.entity.AdminEntity;
import nl.fontys.newswebapplication.repositories.entity.JournalistEntity;
import nl.fontys.newswebapplication.repositories.entity.UserEntity;
import nl.fontys.newswebapplication.services.exceptions.account.UnsupportedAccountTypeException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class AccountConverter {
    public static Account convertToAccount(AccountEntity accountEntity) {
        String profilePicture = null;
        if (accountEntity.getProfilePicture() != null) {
            profilePicture = new String(accountEntity.getProfilePicture());
        }
        if(accountEntity instanceof JournalistEntity journalistEntity) { // Type checking depending on the type due to inheritance
            return Journalist.builder()
                    .id(accountEntity.getId())
                    .fullName(accountEntity.getFullName())
                    .username(accountEntity.getUsername())
                    .dateOfBirth(accountEntity.getDateOfBirth())
                    .profilePicture(profilePicture)
                    .email(accountEntity.getEmail())
                    .password(accountEntity.getPassword())
                    .department(Department.values()[journalistEntity.getDepartmentId()])
                    .build();
        } else if (accountEntity instanceof UserEntity userEntity) {
            return User.builder()
                    .id(accountEntity.getId())
                    .fullName(accountEntity.getFullName())
                    .username(accountEntity.getUsername())
                    .dateOfBirth(accountEntity.getDateOfBirth())
                    .profilePicture(profilePicture)
                    .email(accountEntity.getEmail())
                    .password(accountEntity.getPassword())
                    .bio(userEntity.getBio())
                    .build();
        } else if(accountEntity instanceof AdminEntity adminEntity) {
            return Admin.builder()
                    .id(accountEntity.getId())
                    .fullName(accountEntity.getFullName())
                    .username(accountEntity.getUsername())
                    .dateOfBirth(accountEntity.getDateOfBirth())
                    .profilePicture(profilePicture)
                    .email(accountEntity.getEmail())
                    .password(accountEntity.getPassword())
                    .company(adminEntity.getCompany())
                    .build();
        } else {
            throw new UnsupportedAccountTypeException();
        }
    }

    public static AccountResponse convertToAccountResponse(AccountEntity accountEntity) {
        String profilePicture = null;
        if (accountEntity.getProfilePicture() != null) {
            profilePicture = new String(accountEntity.getProfilePicture());
        }
        if(accountEntity instanceof JournalistEntity journalistEntity) { // Type checking depending on the type due to inheritance
            return JournalistResponse.builder()
                    .id(accountEntity.getId())
                    .fullName(accountEntity.getFullName())
                    .username(accountEntity.getUsername())
                    .dateOfBirth(accountEntity.getDateOfBirth())
                    .profilePicture(profilePicture)
                    .email(accountEntity.getEmail())
                    .department(Department.values()[journalistEntity.getDepartmentId()])
                    .build();
        } else if (accountEntity instanceof UserEntity userEntity) {
            return UserResponse.builder()
                    .id(accountEntity.getId())
                    .fullName(accountEntity.getFullName())
                    .username(accountEntity.getUsername())
                    .dateOfBirth(accountEntity.getDateOfBirth())
                    .profilePicture(profilePicture)
                    .email(accountEntity.getEmail())
                    .bio(userEntity.getBio())
                    .build();
        } else if(accountEntity instanceof AdminEntity adminEntity) {
            return AdminResponse.builder()
                    .id(accountEntity.getId())
                    .fullName(accountEntity.getFullName())
                    .username(accountEntity.getUsername())
                    .dateOfBirth(accountEntity.getDateOfBirth())
                    .profilePicture(profilePicture)
                    .email(accountEntity.getEmail())
                    .company(adminEntity.getCompany())
                    .build();
        } else {
            throw new UnsupportedAccountTypeException();
        }
    }

    public static AccountEntity convertToAccountEntity(Account account) {
            if (account instanceof Journalist journalist) { // Type checking depending on the type due to inheritance
                JournalistEntity journalistEntity = new JournalistEntity();

                journalistEntity.setId(account.getId());
                journalistEntity.setFullName(account.getFullName());
                journalistEntity.setUsername(account.getUsername());
                journalistEntity.setDateOfBirth(account.getDateOfBirth());
                journalistEntity.setEmail(account.getEmail());
                journalistEntity.setPassword(account.getPassword());
                journalistEntity.setDepartmentId(journalist.getDepartment().ordinal());

                if(account.getProfilePicture() != null) {
                    journalistEntity.setProfilePicture(account.getProfilePicture().getBytes());
                } else {
                    journalistEntity.setProfilePicture(null);
                }

                return journalistEntity;
            } else if (account instanceof User user) {
                UserEntity userEntity = new UserEntity();

                userEntity.setId(user.getId());
                userEntity.setFullName(user.getFullName());
                userEntity.setUsername(user.getUsername());
                userEntity.setDateOfBirth(user.getDateOfBirth());
                userEntity.setEmail(user.getEmail());
                userEntity.setPassword(user.getPassword());
                userEntity.setBio(user.getBio());

                if(account.getProfilePicture() != null) {
                    userEntity.setProfilePicture(account.getProfilePicture().getBytes());
                } else {
                    userEntity.setProfilePicture(null);
                }

                return userEntity;
            } else if (account instanceof Admin admin) {
                AdminEntity adminEntity = new AdminEntity();

                adminEntity.setId(admin.getId());
                adminEntity.setFullName(admin.getFullName());
                adminEntity.setUsername(admin.getUsername());
                adminEntity.setDateOfBirth(admin.getDateOfBirth());
                adminEntity.setEmail(admin.getEmail());
                adminEntity.setPassword(admin.getPassword());
                adminEntity.setCompany(admin.getCompany());

                if(account.getProfilePicture() != null) {
                    adminEntity.setProfilePicture(account.getProfilePicture().getBytes());
                } else {
                    adminEntity.setProfilePicture(null);
                }

                return adminEntity;
            } else {
                throw new UnsupportedAccountTypeException();
            }
        }
}
