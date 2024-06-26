package zerobase18.playticketing.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInfo {

    private String name;

    private String phone;

    private String email;

    private String address;

}
