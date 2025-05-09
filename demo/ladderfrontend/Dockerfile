# Build stage
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Production stage
FROM nginx:alpine

# Copy built assets from builder stage
COPY --from=builder /app/dist/ladder-front-end/browser /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"] 

#docker build --no-cache -t ladder-frontend . && docker run -d -p 80:80 ladder-frontend
#craigrivers$ docker tag ladder-frontend craigrivers/ladder-frontend:latest && docker push craigrivers/ladder-frontend:latest