package Models;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Admin
{

    private String Username;
    private String Password;

    public Admin(String Username, String Password)
    {
        this.Username = Username;
        this.Password = Password;
    }

    public String Username()
    {
        return Username;
    }

    public String Password()
    {
        return Password;
    }
}
