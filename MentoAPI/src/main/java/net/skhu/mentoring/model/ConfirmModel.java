package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.enumeration.ResultStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmModel {
    private ResultStatus status;
    private String message;
}
