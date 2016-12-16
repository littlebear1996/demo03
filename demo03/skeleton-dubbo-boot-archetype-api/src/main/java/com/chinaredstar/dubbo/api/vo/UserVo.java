package com.chinaredstar.dubbo.api.vo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.Email;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@ApiModel(value = "UserVo", description = "用户VO")
public class UserVo implements Serializable {
    private static final long serialVersionUID = 2213897933692729976L;
    @ApiModelProperty(value = "user Id,id必须大于3小于5")
    @NotNull
    @Min(value = 3)
    @Max(value = 5)
    private Integer id;
    @ApiModelProperty(value = "user name,必须符合正则表达式^yangguo.*test$")
    @NotNull
    @Pattern(regexp = "^yangguo.*test$")
    private String name;
    @ApiModelProperty(value = "user email,必须满足邮箱格式")
    @Email
    private String email;
    @ApiModelProperty(value = "user password,最短6位最长12位")
    @NotNull
    @Size(min = 6, max = 12)
    private String password;

    public UserVo() {
    }

    public UserVo(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
