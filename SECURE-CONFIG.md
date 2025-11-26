# Secure Configuration Guide

## 概述
为了保护您的GitLab敏感信息（如API Token和Project ID），我们已将配置从`application.properties`文件迁移到环境变量。

## 配置步骤

### 方法1：使用配置脚本（推荐）
```bash
# 运行配置脚本
./setup-gitlab-config.sh

# 按照提示输入您的GitLab配置
```

### 方法2：手动设置环境变量
```bash
# 设置GitLab API配置
export GITLAB_API_URL="https://gitlab.com/api/v4"
export GITLAB_API_TOKEN="your-gitlab-token-here"
export GITLAB_PROJECT_ID="your-project-id-here"

# 启动应用
./gradlew :gitlab4j-stats-demo:bootRun
```

### 方法3：永久配置
将环境变量添加到您的shell配置文件中：

```bash
# 添加到 ~/.zshrc 或 ~/.bashrc
echo 'export GITLAB_API_TOKEN="your-token-here"' >> ~/.zshrc
echo 'export GITLAB_PROJECT_ID="your-project-id"' >> ~/.zshrc

# 重新加载配置
source ~/.zshrc
```

## 环境变量说明

| 变量名 | 说明 | 默认值 | 必需 |
|--------|------|--------|------|
| GITLAB_API_URL | GitLab API地址 | https://gitlab.com/api/v4 | 否 |
| GITLAB_API_TOKEN | GitLab个人访问令牌 | 空 | 是 |
| GITLAB_PROJECT_ID | GitLab项目ID | 空 | 是 |

## 获取GitLab Token

1. 登录GitLab
2. 进入 User Settings → Access Tokens
3. 创建新的个人访问令牌
4. 确保勾选`api`权限

## 获取Project ID

1. 进入您的GitLab项目
2. 在项目页面查看URL中的数字ID
3. 或者在项目设置中查看项目ID

## 安全建议

- ✅ 不要将token提交到代码仓库
- ✅ 使用环境变量存储敏感信息
- ✅ 定期更换API令牌
- ✅ 为不同环境使用不同的令牌
- ✅ 在`.gitignore`中添加`.env.local`文件

## 重启应用

修改配置后，需要重启后端应用：

```bash
# 停止当前应用（如果正在运行）
# 重新启动
./gradlew :gitlab4j-api:bootRun
```

## 验证配置

重启后，可以通过以下API验证配置是否正确：

```bash
# 检查项目信息
curl http://localhost:8081/api/stats/projects

# 检查汇总信息
curl http://localhost:8081/api/stats/summary
```