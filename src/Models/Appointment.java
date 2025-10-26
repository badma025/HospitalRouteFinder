package Models;

import java.time.LocalDate;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Appointment
{
    private int PatientID;
    private int DoctorID;
    private LocalDate Date;
    private String Reason;

    public Appointment(int PatientID, int DoctorID, LocalDate Date, String Reason)
    {
        this.PatientID = PatientID;
        this.DoctorID = DoctorID;
        this.Date = Date;
        this.Reason = Reason;
    }

    public int DoctorID()
    {
        return DoctorID;
    }

    public int PatientID()
    {
        return PatientID;
    }

    public LocalDate Date()
    {
        return Date;
    }

    public String Reason()
    {
        return Reason;
    }


}
