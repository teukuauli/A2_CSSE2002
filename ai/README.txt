I use an ai to check my javadocs is correct, and make an additional javadocs if miss something.
1.ChatGPT 5
    prompt: can you make a perfec javadocs for me and give me the template
    result:
    Absolutely—here’s a clean, copy-pasteable JavaDoc starter kit plus quick rules so your docs look “HD-ready” for marking and teammates.
    Quick rules for “perfect” JavaDocs
    Write what it does first, then why, then any gotchas.
    Use {@code ...} for code literals and {@link Type#member} for links.
    Document every public type/member; inherit with {@inheritDoc} where appropriate.
    Prefer @apiNote, @implSpec, @implNote for deeper context.
    Keep first sentence a single-line summary (ends with a period) → used in indexes.
    Order tags: @param (alphabetical), @return, @throws, @since, @see.