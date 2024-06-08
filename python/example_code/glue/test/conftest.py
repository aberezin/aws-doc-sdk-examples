# Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
# SPDX-License-Identifier: Apache-2.0

"""
Contains common test fixtures used to run AWS Identity and Access Management (IAM)
tests.
"""

import sys

# This is needed so Python can find test_tools on the path.
# sys.path.append("../..")
# append no longer needed since Alan does
# pip install $PYTHON_LIB_AWS_TEST_TOOLS
# Processing /Users/aberezin/Development/aws/aws_test_tools/dist/aws_test_tools-0.1.0-py3-none-any.whl
from aws_test_tools.fixtures.common import *
