import sys
import tiktoken

# Model to tiktoken encoding mapping
MODEL_MAP = {
    "gpt-4": "cl100k_base",
    "gpt-3.5-turbo": "cl100k_base",
    "gpt-4o": "o200k_base",
    "deepseek-v4-pro": "cl100k_base",
}

def count_tokens(text, model="gpt-4"):
    encoding_name = MODEL_MAP.get(model, "cl100k_base")
    enc = tiktoken.get_encoding(encoding_name)
    return len(enc.encode(text))

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: token_counter.py <text> [model]", file=sys.stderr)
        sys.exit(1)
    text = sys.argv[1]
    model = sys.argv[2] if len(sys.argv) > 2 else "gpt-4"
    print(count_tokens(text, model))