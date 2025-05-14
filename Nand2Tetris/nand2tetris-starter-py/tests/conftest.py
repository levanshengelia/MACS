import os

from hypothesis import settings

settings.register_profile("easy", max_examples=5)
settings.register_profile("mild", max_examples=50)
settings.register_profile("hard", max_examples=500)
settings.load_profile(os.getenv("HYPOTHESIS_PROFILE", "default"))
