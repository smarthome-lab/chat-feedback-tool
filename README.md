# chat-feedback-tool

> A tool to view and rate feedback for hs chat

## Requirements

### Linux / Windows (WSL)

```bash
# Install yarn
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
sudo apt-get update && sudo apt-get install yarn

# Install node
curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs build-essential
```

### macOS

```bash
# For homebrew
brew install node@8 yarn

# else check 
# - https://nodejs.org/en/
# - https://yarnpkg.com/en/docs/install
```

## Build Setup

``` bash
# install dependencies
yarn install

# serve with hot reload at localhost:8080
yarn dev

```
