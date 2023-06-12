package com.ll.FlexGym.domain.Meeting;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)
    private String subject;

    @NotNull(message = "모집인원은 필수항목입니다.")
    private Integer capacity;

    @NotEmpty(message = "모임위치는 필수항목입니다.")
    private String location;

    @NotEmpty(message = "모임날짜는 필수항목입니다.")
    private String date;

    @NotEmpty(message = "모임시간은 필수항목입니다.")
    private String time;

    @NotEmpty(message = "모임소개는 필수항목입니다.")
    private String content;
}
