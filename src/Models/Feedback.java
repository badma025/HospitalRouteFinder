package Models;

/**
 * @author  badma025
 * @version 1.0.0
 * @since   2025-10-26
 * Copyright (c) 2025 badma025. ALL RIGHTS RESERVED.
 */

public class Feedback
{
    private int ID;
    private boolean WasHelpful;
    private String Comment;

    public Feedback(boolean WasHelpful, String Comment)
    {
        this.ID = ID;
        this.WasHelpful = WasHelpful;
        this.Comment = Comment;
    }

    public int ID()
    {
        return ID;
    }

    public boolean WasHelpful()
    {
        return WasHelpful;
    }

    public String Comment()
    {
        return Comment;
    }
}
