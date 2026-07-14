from token_counter import count_tokens
text = "Hello, world! This is a test."
tokens = count_tokens(text)
print(f"Tokens: {tokens}")
assert tokens > 0, "Should count tokens"
print("PASS")